package com.angeya.pig.service;

import com.angeya.pig.dao.CityRepository;
import com.angeya.pig.dao.UniversityRepository;
import com.angeya.pig.dao.UserRepository;
import com.angeya.pig.pojo.City;
import com.angeya.pig.pojo.University;
import com.angeya.pig.pojo.User;
import com.angeya.pig.pojo.UserInfo;
import com.angeya.pig.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @ Decs:
 * @ Author: angeya
 * @ Date: 2021/3/29
 */

@Component
public class GenerateTask {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UniversityRepository universityRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    ExecutorService executorService;

    private static final Map<String, University> UNIVERSITY_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, City> CITY_CACHE = new ConcurrentHashMap<>();

    private final Logger logger = LoggerFactory.getLogger(GenerateTask.class);
    private static final int THREAD_NUM = 12;
    volatile static private int REST_USER_NUM = 3_000_000;

    @PostConstruct
    public void start(){
        initCache();
        long startTime = System.currentTimeMillis();
        List<DataGenerator> dataGeneratorList = new ArrayList<>();
        for (int i = 0; i < THREAD_NUM; i++) {
            DataGenerator dataGeneratorTask = new DataGenerator();
            dataGeneratorList.add(dataGeneratorTask);
            dataGeneratorTask.start();
//            executorService.execute(dataGeneratorTask);

        }
        for (int i = 0; i < THREAD_NUM; i++) {
            try {
                dataGeneratorList.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("线程数:{}, 总耗时: {}", THREAD_NUM, System.currentTimeMillis() - startTime);
    }

    private class DataGenerator extends Thread{
        private final Random random = new Random();

        @Override
        public void run() {
            long dt = System.currentTimeMillis();
            while (!isNotFinished()){
                UserInfo userInfo = generateUserInfo();
                if (saveUserInfo(userInfo)) {
                    logger.info("{} 保存成功！", userInfo);
                } else {
                    logger.warn("{} 保存失败！", userInfo);
                }
            }
        }

        /**
         * 生成用户信息
         * @return 用户信息
         */
        private UserInfo generateUserInfo() {
            UserInfo userInfo = new UserInfo();
            userInfo.setName(createName());
            userInfo.setAge((short)(random.nextInt(42) + 20));
            userInfo.setCity(getRandomProperty(Const.CITY));
            userInfo.setGender((byte)random.nextInt(2));
            userInfo.setUniversity(getRandomProperty(Const.UNIVERSITY));
            return userInfo;
        }
        /**
         * 创建名字 = 姓 + （1-2 个字）
         * @return 姓名
         */
        private String createName() {
            String familyName = getRandomProperty(Const.FAMILY_NAME);
            StringBuilder fullName = new StringBuilder(familyName);
            int nameNum = 1 + random.nextInt(2);
            fullName.append(getRandomProperty(Const.FIRST_NAME));
            if (nameNum > 1) {
                fullName.append(getRandomProperty(Const.FIRST_NAME));
            }
            return fullName.toString();
        }

        /**
         * 随机获取数组中的选项
         * @param array 字符串数组
         * @return 随机字符串
         */
        private String getRandomProperty(String[] array){
            return array[random.nextInt(array.length)];
        }

        /**
         * 保存用户信息
         * @param userInfo 用户信息
         */
        private boolean saveUserInfo(UserInfo userInfo) {
            University university;
            City city;
            if (UNIVERSITY_CACHE.containsKey(userInfo.getUniversity())) {
                university = UNIVERSITY_CACHE.get(userInfo.getUniversity());
            } else {
                university = universityRepository.save(new University(userInfo.getUniversity()));
                UNIVERSITY_CACHE.put(university.getName(), university);
            }
            if (CITY_CACHE.containsKey(userInfo.getCity())) {
                city = CITY_CACHE.get(userInfo.getCity());
            } else {
                city = cityRepository.save(new City(userInfo.getCity()));
                CITY_CACHE.put(city.getName(), city);
            }
            if (university != null && city != null) {
                userRepository.save(new User(userInfo.getName(), userInfo.getGender(), userInfo.getAge(), university.getId(), city.getId()));
                return true;
            }
            return false;
        }
    }
    /**
     * 初始化大学和城市缓存
     */
    private void initCache(){
        List<University> universityList = universityRepository.findAll();
        for (University university : universityList) {
            UNIVERSITY_CACHE.put(university.getName(), university);
        }

        List<City> cityList = cityRepository.findAll();
        for (City city : cityList) {
            CITY_CACHE.put(city.getName(), city);
        }
    }

    private synchronized boolean isNotFinished() {
        if (REST_USER_NUM > 0) {
            logger.info("正在保存倒数第 {} 条数据", REST_USER_NUM);
            REST_USER_NUM --;
            return false;
        }
        return true;
    }
}
