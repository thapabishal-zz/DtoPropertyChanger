import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static DtoPropertyUtil.copyValue;
import static DtoPropertyUtil.setDefaultValue;
import static org.junit.Assert.assertEquals;


public class DtoPropertyUtilTestCase {

    @Test
    public void testNoExceptionWhenNullReferencePassedToSetValue()  {
        setDefaultValue(null, null);
    }

    @Test
    public void testNoExceptionWhenNullReferencePassedForCopy()  {
        copyValue(null, null, null);
    }

    @Test
    public void testSetDefaultValueToPrimitiveProperty() {
        CompanyConfigDTO company = new CompanyConfigDTO();
        company.setAllowedUsersCount(100);
        company.setCarrier(true);
        company.setExistingUsersCount(100L);
        // when
        setDefaultValue(company, "allowedUsersCount", "carrier", "existingUsersCount");
        // then
        assertEquals(false, company.isCarrier());
        assertEquals(0, company.getExistingUsersCount(), Integer.valueOf(0));
        assertEquals(0, company.getAllowedUsersCount(), Integer.valueOf(0));

    }

    @Test
    public void testSetDefaultValueToWrapperProperty() {
        UserDTO userDTO = new UserDTO();
        userDTO.setMainContact(Boolean.TRUE);
        userDTO.setApplications("Admin");
        userDTO.setLastLogin(new Date());
        userDTO.setIamStatus(IAMStatus.ENABLED);
        // when
        setDefaultValue(userDTO, "mainContact", "applications", "lastLogin", "iamStatus");
        // then
        assertEquals(false, userDTO.getMainContact());
        assertEquals(null, userDTO.getApplications());
        assertEquals(null, userDTO.getLastLogin());
        assertEquals(null, userDTO.getIamStatus());

    }

    @Test
    public void testSetDefaultValueForNestedObjectProperty() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserRole(new DictionaryDTO(1l, "SuperUser"));
        //when
        setDefaultValue(userDTO, "mainContact", "userRole.value");
        //then
        assertEquals(null, userDTO.getUserRole().getValue());
    }

    @Test
    public void copyDtoValue() {
        UserDTO sourceDtoSafe = new UserDTO();
        sourceDtoSafe.setUserRole(new DictionaryDTO(1L, "User"));
        sourceDtoSafe.setApplications("TWA");
        sourceDtoSafe.setMainContact(Boolean.FALSE);
        sourceDtoSafe.setCompanyId(5001);

        UserDTO dtoFromUI = new UserDTO(); // bad guy messed some properties
        dtoFromUI.setUserRole(new DictionaryDTO(2L, "Admin"));
        dtoFromUI.setApplications("CD");
        dtoFromUI.setMainContact(Boolean.TRUE);
        dtoFromUI.setCompanyId(9001);

        // when
        copyValue(sourceDtoSafe, dtoFromUI, "userRole", "applications", "mainContact", "companyId");
        // then
        assertEquals("User", dtoFromUI.getUserRole().getValue());
        assertEquals("Admin",  dtoFromUI.getApplications());
        assertEquals(Boolean.FALSE,  dtoFromUI.getMainContact());
        assertEquals(5001,  dtoFromUI.getCompanyId());
    }

}