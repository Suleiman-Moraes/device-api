package com.moraes.device_api.api.model.dto.interfaces;

import org.springframework.data.domain.Sort.Direction;

public interface IFilterDTO {

    Direction getDirection();
    void setDirection(Direction direction);

    String getProperty();
    void setProperty(String property);

    int getPage();
    void setPage(int page);

    int getSize();
    void setSize(int size);

    boolean isPaginate();
    void setPaginate(boolean paginate);
    
    String getSearchText();
    void setSearchText(String searchText);
}
