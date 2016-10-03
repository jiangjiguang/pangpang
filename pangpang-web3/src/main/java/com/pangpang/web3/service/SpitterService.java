package com.pangpang.web3.service;

import com.pangpang.web3.domain.Spittle;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 16-10-2.
 */
@Service
public class SpitterService {
    public Object getRecentSplittles(int pageSize){
        System.out.println("++++++++++++++++++++");
        System.out.println("getRecentSplittles");
        List<Spittle> expectedSpittles = Arrays.asList(new Spittle(), new Spittle(), new Spittle());
        return expectedSpittles;
    }
}
