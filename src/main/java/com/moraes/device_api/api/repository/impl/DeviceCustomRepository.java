package com.moraes.device_api.api.repository.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.moraes.device_api.api.model.dto.device.DeviceFilterDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.api.repository.IDeviceCustomRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class DeviceCustomRepository implements IDeviceCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<DeviceListDTO> findByFilter(DeviceFilterDTO filter) {
        if (!StringUtils.hasText(filter.getProperty())) {
            filter.setProperty("id");
        }
        if(filter.getDirection() == null) {
            filter.setDirection(Direction.DESC);
        }
        final Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(),
                Sort.by(filter.getDirection(), filter.getProperty()));
        try {
            final List<DeviceListDTO> lista = listByFilter(filter);
            final Integer total = filter.isPaginate() ? countByFilter(filter) : lista.size();
            return new PageImpl<>(lista, pageable, total);
        } catch (Exception e) {
            log.warn("findByFilter {}", e.getMessage(), e);
        }
        return new PageImpl<>(new LinkedList<>(), pageable, 0);
    }

    /**
     * This method is used to list devices according to the given filter.
     * It constructs a query based on the filter properties and then executes it.
     * If the filter is paginated, it sets the max results and the first result.
     * If an exception occurs, it logs the error and returns null.
     * 
     * @param filter the device filter
     * @return the list of devices or null if an exception occurs
     */
    @SuppressWarnings("unchecked")
    public List<DeviceListDTO> listByFilter(DeviceFilterDTO filter) {
        try {
            StringBuilder sql = new StringBuilder("SELECT");
            sql.append(" item.id,");
            sql.append(" item.name,");
            sql.append(" item.brand,");
            sql.append(" item.state,");
            sql.append(" item.creation_time AS creationTime");
            sql.append(" ");
            Query query = getQueryByFilter(filter, sql.toString(),
                    String.format(" ORDER BY item.%s %s", filter.getProperty(),
                            filter.getDirection().name()),
                    DeviceListDTO.DEVICE_LIST_DTO_MAPPING);
            if (filter.isPaginate()) {
                return query.setMaxResults(filter.getSize())
                        .setFirstResult(filter.getSize() * filter.getPage()).getResultList();
            }
            return query.getResultList();

        } catch (Exception e) {
            log.warn("listByFilter {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * This method is used to count devices according to the given filter.
     * It constructs a query based on the filter properties and then executes it.
     * If the filter is paginated, it sets the max results and the first result.
     * If an exception occurs, it logs the error and returns 0.
     * 
     * @param filter the device filter
     * @return the count of devices or 0 if an exception occurs
     */
    public Integer countByFilter(DeviceFilterDTO filter) {
        try {
            return Integer.valueOf(getQueryByFilter(filter, "SELECT COUNT(item.id) ", "", null)
                    .getSingleResult().toString());
        } catch (Exception e) {
            log.warn("countByFilter {}", e.getMessage(), e);
        }
        return 0;
    }

    /**
     * This method is used to build a query based on the given filter.
     * It takes four parameters: the filter, the initial SQL string, a complement
     * string and a mapping name.
     * It first appends the FROM clause to the SQL string and then appends the WHERE
     * clause.
     * It then applies the filters to the query by calling applyFilters() and
     * appending the complement string.
     * Finally, it creates a native query with the given mapping name and sets the
     * parameters from the map.
     * If an exception occurs, it logs the error and throws the exception.
     * 
     * @param filter      the device filter
     * @param sql         the initial SQL string
     * @param complement  the string to append to the SQL string after the WHERE
     *                    clause
     * @param mappingName the mapping name to use when creating the native query
     * @return the query object
     */
    public Query getQueryByFilter(DeviceFilterDTO filter, String sql, String complement, String mappingName) {
        try {
            Map<String, Object> map = new HashMap<>();
            StringBuilder stringBuilder = new StringBuilder(sql);
            stringBuilder.append(" FROM device_api.device item");
            stringBuilder.append(" WHERE 1 = 1");
            applyFilters(filter, map, stringBuilder);
            stringBuilder.append(" ").append(complement);
            Query query = null;
            if (mappingName == null) {
                query = entityManager.createNativeQuery(stringBuilder.toString());
            } else {
                query = entityManager.createNativeQuery(stringBuilder.toString(), mappingName);
            }
            for (String key : map.keySet()) {
                query.setParameter(key, map.get(key));
            }
            return query;
        } catch (Exception e) {
            log.warn("getQueryByFilter " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method is used to apply the given filter to the query.
     * It takes three parameters: the filter, the map to store the parameters and
     * the string builder.
     * It first checks if the filter has a brand, if so, it appends the brand filter
     * to the SQL string and adds the brand to the map.
     * It then checks if the filter has a name, if so, it appends the name filter to
     * the SQL string and adds the name to the map.
     * Finally, it checks if the filter has a searchText, if so, it appends the
     * searchText filter to the SQL string and adds the searchText to the map.
     * 
     * @param filter        the device filter
     * @param map           the map to store the parameters
     * @param stringBuilder the string builder to append the filters to
     */
    public void applyFilters(DeviceFilterDTO filter, Map<String, Object> map, StringBuilder stringBuilder) {
        if (StringUtils.hasText(filter.getBrand())) {
            stringBuilder.append(" AND item.brand = :brand");
            map.put("brand", filter.getBrand());
        }
        if (StringUtils.hasText(filter.getName())) {
            stringBuilder.append(" AND item.name ILIKE :name");
            map.put("name", "%" + filter.getName().trim() + "%");
        }
        if(filter.getState() != null) {
            stringBuilder.append(" AND item.state = :state");
            map.put("state", filter.getState().name());
        }
        if (StringUtils.hasText(filter.getSearchText())) {
            stringBuilder.append(" AND (item.name ILIKE :searchText ");
            stringBuilder.append(" OR item.brand ILIKE :searchText ");
            stringBuilder.append(" OR item.state ILIKE :searchText ");
            stringBuilder.append(" OR TO_CHAR(item.creation_time, 'YYYY-MM-DD HH24:MI:SS') ILIKE :searchText) ");
            map.put("searchText", "%" + filter.getSearchText().trim() + "%");
        }
    }
}
