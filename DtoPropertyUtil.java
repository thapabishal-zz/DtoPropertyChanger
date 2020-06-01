
import org.apache.commons.beanutils.PropertyUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class DtoPropertyUtil {

    private static final Logger LOG = Logger.getLogger(String.valueOf(DtoPropertyUtil.class));

    /**
     * Copies the property values of the sourceDto to the properties of the targetDto
     * sourceDto and targetDto should be of same class
     *
     * @param sourceDto
     * @param targetDto
     */
    public static void copyValue(Serializable sourceDto, Serializable targetDto, String... propertiesNames) {
        boolean condition = Optional.ofNullable(sourceDto).isPresent() && Optional.ofNullable(targetDto).isPresent() && sourceDto.getClass().equals(targetDto.getClass());
        if (condition)
            copy(sourceDto, targetDto, propertiesNames);
    }

    /**
     * This method sets the default value to the property of the dto
     *
     * @param dto
     */
    public static void setDefaultValue(Serializable dto, String... propertiesNames) {
        if (Optional.ofNullable(dto).isPresent() && Optional.ofNullable(propertiesNames).isPresent())
            setValue(dto, propertiesNames);
    }

    /**
     * This method sets the default value to the property of the dto
     *
     * @param dtoList
     */
    public static void setDefaultValue(List<? extends Serializable> dtoList, String... propertiesNames) {
        if (Optional.ofNullable(dtoList).isPresent() && Optional.ofNullable(propertiesNames).isPresent()) {
            dtoList.forEach( dto -> setDefaultValue(dto, propertiesNames) );
        }
    }

    private static void setValue(Serializable dto, String... propertiesNames) {
        Arrays.asList(propertiesNames).stream().forEach(fieldName -> {
            try {
                Object property = PropertyUtils.getNestedProperty(dto, fieldName);
                Object defaultValue = getDefaultValue(property.getClass().getSimpleName());
                PropertyUtils.setNestedProperty(dto, fieldName, defaultValue);
            } catch (Exception e) {
                LOG.info("Could not set default value, Exception thrown " + e.getLocalizedMessage());
            }
        });
    }

    private static void copy(Serializable sourceDto, Serializable targetDto, String... propertiesNames) {
        List<String> propertyNameList = Arrays.asList(propertiesNames);
        propertyNameList.stream().forEach(fieldName -> {
            Object value;
            try {
                value = PropertyUtils.getNestedProperty(sourceDto, fieldName);
                PropertyUtils.setNestedProperty(targetDto, fieldName, value);
            } catch (Exception e) {
                LOG.info("Could not copy value, Exception thrown " + e.getMessage());
            }
        });
    }

    private static Object getDefaultValue(String classTypeName) {
        if (Long.class.getSimpleName().equalsIgnoreCase(classTypeName) || long.class.getSimpleName().equalsIgnoreCase(classTypeName))
            return Long.valueOf(0);
        if (Boolean.class.getSimpleName().equalsIgnoreCase(classTypeName) || boolean.class.getSimpleName().equalsIgnoreCase(classTypeName))
            return Boolean.FALSE;
        if (Integer.class.getSimpleName().equalsIgnoreCase(classTypeName) || int.class.getSimpleName().equalsIgnoreCase(classTypeName))
            return Integer.valueOf(0);
        if (Double.class.getSimpleName().equalsIgnoreCase(classTypeName) || double.class.getSimpleName().equalsIgnoreCase(classTypeName))
            return Double.valueOf(0);
        if (Float.class.getSimpleName().equalsIgnoreCase(classTypeName) || float.class.getSimpleName().equalsIgnoreCase(classTypeName))
            return Float.valueOf(0);
        if (Short.class.getSimpleName().equalsIgnoreCase(classTypeName) || short.class.getSimpleName().equalsIgnoreCase(classTypeName))
            return Short.valueOf((short) 0);
        return null;
    }

}


