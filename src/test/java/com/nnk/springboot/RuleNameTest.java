package com.nnk.springboot;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.service.RuleNameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RuleNameTest {

    @InjectMocks
    RuleNameService ruleNameServiceTest;

    @Mock
    RuleNameRepository ruleNameRepositoryMock;

    private final RuleName ruleName = new RuleName();

    @Test
    public void createRuleNameDoesNotExistShouldCallTheRuleNameRepositorySaveMethodTest() {

        ruleNameServiceTest.createRuleName(ruleName);

        verify(ruleNameRepositoryMock, Mockito.times(1)).save(ruleName);
    }

    @Test
    public void updateRuleNameShouldCallTheRuleNameRepositorySaveMethodTest() {

        ruleNameServiceTest.updateRuleName(ruleName);

        verify(ruleNameRepositoryMock, Mockito.times(1)).save(ruleName);
    }

    @Test
    public void deleteRuleNameShouldCallTheRuleNameRepositoryDeleteMethodTest() {

        ruleNameServiceTest.deleteRuleName(ruleName);

        verify(ruleNameRepositoryMock, Mockito.times(1)).delete(ruleName);
    }

    @Test
    public void loadRuleNameListShouldCallTheRuleNameRepositoryFindAllMethodTest() {

        ruleNameServiceTest.loadRuleNameList();

        verify(ruleNameRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void loadRuleNameByIdShouldCallTheRuleNameRepositoryFindByIdMethodTest() {

        when(ruleNameRepositoryMock.findById(anyInt())).thenReturn(Optional.of(ruleName));

        ruleNameServiceTest.loadRuleNameById(anyInt());

        verify(ruleNameRepositoryMock, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void loadRuleNameByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> ruleNameServiceTest.loadRuleNameById(2));
        assertEquals("Invalid RuleName Id:" + 2, exception.getMessage());
    }
}