package com.wsmhz.security.oauth.service.domain.entity;
import com.wsmhz.common.business.domain.Domain;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * create by tangbj on 2018/4/25
 * 需要控制权限的资源，以业务人员能看懂的name呈现.实际关联到一个或多个url上。
 * 树形结构。
 */
@Table(name = "resource")
public class Resource extends Domain {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 父资源
     */
    private Long parentId;
    /**
     * 资源名称，如xx菜单，xx按钮
     */
    private String name;
    /**
     * 资源链接
     */
    private String url;
    /**
     * 图标
     */
    private String icon;
    /**
     * 资源类型
     */
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private ResourceType type;
    /**
     * 序号
     */
    private int sort;

    @Transient
    private List<Resource> children = new ArrayList<>();

    public Resource() {
    }

    public Resource(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<Resource> getChildren() {
        return children;
    }

    public void setChildren(List<Resource> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(id, resource.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
