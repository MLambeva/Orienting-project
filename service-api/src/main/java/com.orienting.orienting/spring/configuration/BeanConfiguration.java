package com.orienting.orienting.spring.configuration;

import com.orienting.common.dto.CompetitorsAndCoachDto;
import com.orienting.common.dto.CompetitorDto;
import com.orienting.common.dto.UserDto;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.repository.ClubRepository;
import com.orienting.common.repository.CompetitionRepository;
import com.orienting.common.repository.UserRepository;
import com.orienting.common.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { //
        "com.orienting.common.repository" //
})
@EntityScan(basePackages = { //
        "com.orienting.common.entity" //
})
public class BeanConfiguration {

    @Bean
    public UserService userService(UserRepository repository) { return new UserService(repository); }
    @Bean
    public ClubService clubService(ClubRepository clubRepository) { return new ClubService(clubRepository); }
    @Bean
    public UserClubService userClubService(UserRepository userRepository, ClubRepository clubRepository) { return new UserClubService(userRepository, clubRepository); }
    @Bean
    public CompetitionService competitionService(CompetitionRepository competitionRepository) { return new CompetitionService(competitionRepository); }

    @Bean
    public UserCompetitionService userCompetitionService(UserRepository userRepository, CompetitionRepository competitionRepository) {
        return new UserCompetitionService(userRepository, competitionRepository);
    }
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(UserEntity.class, UserDto.class)
                .addMapping(src -> src.getClub().getCity(), UserDto::setCity);
        modelMapper.createTypeMap(UserEntity.class, CompetitorDto.class)
                .addMapping(src -> src.getClub().getCity(), CompetitorDto::setCity);
        return modelMapper;
    }
   /* @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }*/

}
