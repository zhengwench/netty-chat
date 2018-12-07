package chat.core.db.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @auther a-de
 * @date 2018/10/12 13:47
 */
public abstract class AbstractBaseObject implements java.io.Serializable {
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.DEFAULT_STYLE, false);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
