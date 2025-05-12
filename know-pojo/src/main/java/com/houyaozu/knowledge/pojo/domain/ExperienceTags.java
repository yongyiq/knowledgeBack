package com.houyaozu.knowledge.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName experience_tags
 */
@TableName(value ="experience_tags")
@Data
public class ExperienceTags {
    /**
     * 
     */
    private Integer experienceId;

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
        ExperienceTags other = (ExperienceTags) that;
        return (this.getExperienceId() == null ? other.getExperienceId() == null : this.getExperienceId().equals(other.getExperienceId()))
            && (this.getTagId() == null ? other.getTagId() == null : this.getTagId().equals(other.getTagId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getExperienceId() == null) ? 0 : getExperienceId().hashCode());
        result = prime * result + ((getTagId() == null) ? 0 : getTagId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", experienceId=").append(experienceId);
        sb.append(", tagId=").append(tagId);
        sb.append("]");
        return sb.toString();
    }
}