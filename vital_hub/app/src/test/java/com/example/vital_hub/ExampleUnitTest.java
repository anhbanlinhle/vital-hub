package com.example.vital_hub;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.home_page.HomePagePost;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void homepage_check_correct() {
        int result = HomePageActivity.homepageCheck("header", 7L);
        assertEquals(result, 1);
    }

    @Test
    public void homepage_check_incorrect1() {
        int result = HomePageActivity.homepageCheck("not_header", 1L);
        assertEquals(result, 0);
    }

    @Test
    public void homepage_check_incorrect2() {
        int result = HomePageActivity.homepageCheck("not_header", 7L);
        assertEquals(result, 0);
    }

    @Test
    public void homepage_check_incorrect3() {
        int result = HomePageActivity.homepageCheck("header", 1L);
        assertEquals(result, 0);
    }

    @Test
    public void comment_check_correct() {
        int result = HomePageActivity.homepageCheck("header", 7L);
        assertEquals(result, 1);
    }
}