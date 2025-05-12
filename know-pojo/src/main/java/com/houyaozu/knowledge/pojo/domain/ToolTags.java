package com.houyaozu.knowledge.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName tool_tags
 */
@TableName(value ="tool_tags")
@Data
public class ToolTags {
    /**
     * 
     */
    private Integer toolId;

    /**
     * 
     */
    private Integer tagId;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ToolTags other = (ToolTags) that;
        return (this.getToolId() == null ? other.getToolId() == null : this.getToolId().equals(other.getToolId()))
            && (this.getTagId() == null ? other.getTagId() == null : this.getTagId().equals(other.getTagId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getToolId() == null) ? 0 : getToolId().hashCode());
        result = prime * result + ((getTagId() == null) ? 0 : getTagId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", toolId=").append(toolId);
        sb.append(", tagId=").append(tagId);
        sb.append("]");
        return sb.toString();
    }
}