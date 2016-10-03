package com.pangpang.web3.controller;

import com.pangpang.web3.domain.Spittle;
import com.pangpang.web3.service.SpitterService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 16-10-2.
 */
public class HomeControllerTest {

    @Test
    public void shouldDisplayRecentSpittles(){
        List<Spittle> expectedSpittles = Arrays.asList(new Spittle(), new Spittle(), new Spittle());
        SpitterService spitterService = Mockito.mock(SpitterService.class);

        System.out.println(spitterService);
        System.out.println(spitterService.getRecentSplittles(1));

        Mockito.when(spitterService.getRecentSplittles(1)).thenReturn(expectedSpittles);

        HomeController controller = new HomeController(spitterService);

        Map<String, Object> model = new HashMap<>();

        String viewName = controller.showHomePage(model);

        Assert.assertEquals("home", viewName);

        Assert.assertSame(expectedSpittles, model.get("spittles"));

        Mockito.verify(spitterService).getRecentSplittles(1);
    }
}
