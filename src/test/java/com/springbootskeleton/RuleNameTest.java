package com.springbootskeleton;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.service.RuleNameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RuleNameTest {

    @InjectMocks
    RuleNameService ruleNameServiceTest;

    @Mock
    RuleNameRepository ruleNameRepositoryMock;

    private RuleNameDto ruleNameDto = new RuleNameDto();

    private RuleName ruleName = new RuleName();

    @Test
    public void saveRuleNameDoesNotExistShouldCallTheRuleNameRepositorySaveMethodTest() {

        ruleNameServiceTest.saveRuleName(ruleNameDto);

        verify(ruleNameRepositoryMock, Mockito.times(1)).save(any(RuleName.class));
    }

    @Test
    public void updateRuleNameShouldCallTheRuleNameRepositorySaveMethodTest() {

        ruleName = new RuleName(
                1,
                "name",
                "description",
                "json",
                "template",
                "sqlStr",
                "sqlPart"
        );

        ruleNameDto = new RuleNameDto(
                1,
                "name2",
                "description2",
                "json2",
                "template2",
                "sqlStr2",
                "sqlPart2"
        );


        ruleNameServiceTest.updateRuleName(ruleName, ruleNameDto);

        verify(ruleNameRepositoryMock, Mockito.times(1)).save(any(RuleName.class));
    }

    @Test
    public void deleteRuleNameShouldCallTheRuleNameRepositoryDeleteMethodTest() {

        ruleNameServiceTest.deleteRuleName(ruleName);

        verify(ruleNameRepositoryMock, Mockito.times(1)).delete(ruleName);
    }

    @Test
    public void loadRuleNameDtoListShouldReturnAllTheRuleNamesDtoTest() {

        List<RuleName> ruleNameList = new ArrayList<>(List.of(
                new RuleName(
                        1,
                        "name",
                        "description",
                        "json",
                        "template",
                        "sqlStr",
                        "sqlPart"
                )
        ));

        when(ruleNameRepositoryMock.findAll()).thenReturn(ruleNameList);

        List<RuleNameDto> ruleNameDtoList = ruleNameServiceTest.loadRuleNameDtoList();

        verify(ruleNameRepositoryMock, Mockito.times(1)).findAll();
        assertEquals(ruleNameList.get(0).getId(), ruleNameDtoList.get(0).getId());
        assertEquals(ruleNameList.get(0).getName(), ruleNameDtoList.get(0).getName());
        assertEquals(ruleNameList.get(0).getDescription(), ruleNameDtoList.get(0).getDescription());
        assertEquals(ruleNameList.get(0).getJson(), ruleNameDtoList.get(0).getJson());
        assertEquals(ruleNameList.get(0).getTemplate(), ruleNameDtoList.get(0).getTemplate());
        assertEquals(ruleNameList.get(0).getSqlStr(), ruleNameDtoList.get(0).getSqlStr());
        assertEquals(ruleNameList.get(0).getSqlPart(), ruleNameDtoList.get(0).getSqlPart());
    }

    @Test
    public void loadRuleNameDtoByIdShouldReturnARuleNameDtoTest() {

        RuleName ruleName = new RuleName(
                1,
                "name",
                "description",
                "json",
                "template",
                "sqlStr",
                "sqlPart"
        );

        when(ruleNameRepositoryMock.findById(ruleName.getId())).thenReturn(Optional.of(ruleName));

        RuleNameDto ruleNameDto = ruleNameServiceTest.loadRuleNameDtoById(ruleName.getId());

        verify(ruleNameRepositoryMock, Mockito.times(1)).findById(ruleName.getId());
        assertEquals(ruleName.getId(), ruleNameDto.getId());
        assertEquals(ruleName.getName(), ruleNameDto.getName());
        assertEquals(ruleName.getDescription(), ruleNameDto.getDescription());
        assertEquals(ruleName.getJson(), ruleNameDto.getJson());
        assertEquals(ruleName.getTemplate(), ruleNameDto.getTemplate());
        assertEquals(ruleName.getSqlStr(), ruleNameDto.getSqlStr());
        assertEquals(ruleName.getSqlPart(), ruleNameDto.getSqlPart());
    }

    @Test
    public void loadRuleNameDtoByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> ruleNameServiceTest.loadRuleNameDtoById(2));
        assertEquals("Invalid RuleName Id:" + 2, exception.getMessage());
    }

    @Test
    public void mapToRuleNameDTOShouldReturnARuleNameDtoFromARuleNameEntityTest() {

        RuleName ruleName = new RuleName(
                1,
                "name",
                "description",
                "json",
                "template",
                "sqlStr",
                "sqlPart"
        );

        RuleNameDto ruleNameDto = ruleNameServiceTest.mapToRuleNameDTO(ruleName);

        assertEquals(ruleName.getId(), ruleNameDto.getId());
        assertEquals(ruleName.getName(), ruleNameDto.getName());
        assertEquals(ruleName.getDescription(), ruleNameDto.getDescription());
        assertEquals(ruleName.getJson(), ruleNameDto.getJson());
        assertEquals(ruleName.getTemplate(), ruleNameDto.getTemplate());
        assertEquals(ruleName.getSqlStr(), ruleNameDto.getSqlStr());
        assertEquals(ruleName.getSqlPart(), ruleNameDto.getSqlPart());
    }
}
