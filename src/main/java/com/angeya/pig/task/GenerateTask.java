package com.angeya.pig.task;

import com.angeya.pig.dao.CityRepository;
import com.angeya.pig.dao.UniversityRepository;
import com.angeya.pig.dao.UserRepository;
import com.angeya.pig.pojo.City;
import com.angeya.pig.pojo.Person;
import com.angeya.pig.pojo.University;
import com.angeya.pig.pojo.User;
import com.angeya.pig.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

    // 缓存 <名称, id>
    private static Map<String, University> universityCache = new ConcurrentHashMap<>();
    private static Map<String, City> cityCache = new ConcurrentHashMap<String, City>();

    private final Logger logger = LoggerFactory.getLogger(GenerateTask.class);
    private final int THREAD_NUM = 5;
    volatile private int REST_USER_NUM = 1000;

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
            while (!isFinished()){
                Person person = generatePerson();
                savePerson(person);
                synchronized (this) {
                    logger.info("第 {} 条数据保存成功: {}", REST_USER_NUM, person);
                }
            }
            logger.info("线程: {}, 耗时: {}", Thread.currentThread().getName(), System.currentTimeMillis() - dt);
        }

        private Person generatePerson() {
            Person person = new Person();
            person.setName(createName());
            person.setAge((short)(random.nextInt(42) + 20));
            person.setCity(getRandomProperty(Const.CITY));
            person.setGender((byte)random.nextInt(2));
            person.setUniversity(getRandomProperty(Const.UNIVERSITY));
            return person;
        }

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

        private String getRandomProperty(String[] array){
            return array[random.nextInt(array.length)];
        }

        @Transactional
        private boolean savePerson(Person person) {
            University university;
            City city;
            if (universityCache.containsKey(person.getUniversity())) {
                university = universityCache.get(person.getUniversity());
            } else {
                university = universityRepository.save(new University(person.getUniversity()));
                universityCache.put(university.getName(), university);
            }
            if (cityCache.containsKey(person.getCity())) {
                city = cityCache.get(person.getCity());
            } else {
                city = cityRepository.save(new City(person.getCity()));
                cityCache.put(city.getName(), city);
            }
            userRepository.save(new User(person.getName(), person.getGender(), person.getAge(), university.getId(), city.getId()));
            return true;
        }

        private boolean saveUser(Person person) {
            return true;
        }
    }

    private void initCache(){
        List<University> universityList = universityRepository.findAll();
        for (University university : universityList) {
            universityCache.put(university.getName(), university);
        }

        List<City> cityList = cityRepository.findAll();
        for (City city : cityList) {
            cityCache.put(city.getName(), city);
        }
    }

    private synchronized boolean isFinished() {
        if (REST_USER_NUM > 0) {
            REST_USER_NUM --;
            return false;
        }
        return true;
    }
}
