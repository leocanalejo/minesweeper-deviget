package com.lcanalejo.deviget.minesweeper.api.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcanalejo.deviget.minesweeper.dto.CreateGame;
import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.dto.User;
import com.lcanalejo.deviget.minesweeper.security.dto.JwtResponse;
import com.lcanalejo.deviget.minesweeper.security.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class MinesweeperClientImpl implements MinesweeperClient {

    private final String baseUrl;
    private ObjectMapper objectMapper = new ObjectMapper();
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void createUser(User user) {
        String url = baseUrl + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        log.info("Executing: " + url + "with body: " + user);

        try {
            this.restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(user, headers), Void.class);
        } catch (HttpClientErrorException e) {
            log.error("Not able to get the response from url: {}", url);
            throw new RestClientException(String.format("Not able to get the response from url: %s", url), e);
        }
    }

    @Override
    public JwtResponse authenticate(LoginRequest loginRequest) {
        String url = baseUrl + "/authenticate";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        log.info("Executing: " + url + " with body " + loginRequest);

        try {
            return this.restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(loginRequest, headers), JwtResponse.class).getBody();
        } catch (HttpClientErrorException e) {
            log.error("Not able to get the response from url: {}", url);
            throw new RestClientException(String.format("Not able to get the response from url: %s", url), e);
        }
    }

    @Override
    public Game createGame(CreateGame createGame, String jwt) {
        String url = baseUrl + "/games";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + jwt);

        log.info("Executing: " + url + "with body: " + createGame);

        try {
            return this.restTemplate.postForEntity(url, new HttpEntity(createGame, headers), Game.class).getBody();
        } catch (HttpClientErrorException e) {
            log.error("Not able to get the response from url: {}", url);
            throw new RestClientException(String.format("Not able to get the response from url: %s", url), e);
        }
    }

    @Override
    public void deleteGame(Long gameId, String jwt) {
        String url = baseUrl + "/games/" + gameId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        log.info("Executing: " + url);

        try {
            this.restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity(null, headers), Void.class);
        } catch (HttpClientErrorException e) {
            log.error("Not able to get the response from url: {}", url);
            throw new RestClientException(String.format("Not able to get the response from url: %s", url), e);
        }
    }

    @Override
    public Game getGame(Long gameId, String jwt) {
        String url = baseUrl + "/games/" + gameId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        log.info("Executing: " + url);

        try {
            return this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(null, headers), Game.class).getBody();
        } catch (HttpClientErrorException e) {
            log.error("Not able to get the response from url: {}", url);
            throw new RestClientException(String.format("Not able to get the response from url: %s", url), e);
        }
    }

    @Override
    public Page<Game> getGames(Pageable paginated, String jwt) {
        String url = baseUrl + "/games";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + jwt);

        log.info("Executing: " + url + " with body: " + paginated);

        try {
            ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(paginated, headers), new ParameterizedTypeReference<String>() {});
            return objectMapper.readValue(response.getBody(), new TypeReference<Page<Game>>() {});
        } catch (HttpClientErrorException | IOException e) {
            log.error("Not able to get the response from url: {}", url);
            throw new RestClientException(String.format("Not able to get the response from url: %s", url), e);
        }
    }

    @Override
    public Game startGame(Long gameId, String jwt) {
        String url = baseUrl + "/games/" + gameId + "/start";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        log.info("Executing: " + url);

        try {
            return this.restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity(null, headers), Game.class).getBody();
        } catch (HttpClientErrorException e) {
            log.error("Not able to get the response from url: {}", url);
            throw new RestClientException(String.format("Not able to get the response from url: %s", url), e);
        }
    }

    @Override
    public Game pauseGame(Long gameId, String jwt) {
        String url = baseUrl + "/games/" + gameId + "/pause";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        log.info("Executing: " + url);

        try {
            return this.restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity(null, headers), Game.class).getBody();
        } catch (HttpClientErrorException e) {
            log.error("Not able to get the response from url: {}", url);
            throw new RestClientException(String.format("Not able to get the response from url: %s", url), e);
        }
    }

    @Override
    public Game flagCell(Long cellId, String jwt) {
        String url = baseUrl + "/cells/" + cellId + "/flag";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        log.info("Executing: " + url);

        try {
            return this.restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity(null, headers), Game.class).getBody();
        } catch (HttpClientErrorException e) {
            log.error("Not able to get the response from url: {}", url);
            throw new RestClientException(String.format("Not able to get the response from url: %s", url), e);
        }
    }

    @Override
    public Game unflagCell(Long cellId, String jwt) {
        String url = baseUrl + "/cells/" + cellId + "/flag";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        log.info("Executing: " + url);

        try {
            return this.restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity(null, headers), Game.class).getBody();
        } catch (HttpClientErrorException e) {
            log.error("Not able to get the response from url: {}", url);
            throw new RestClientException(String.format("Not able to get the response from url: %s", url), e);
        }
    }

    @Override
    public Game revealCell(Long cellId, String jwt) {
        String url = baseUrl + "/games/" + cellId + "/reveal";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        log.info("Executing: " + url);

        try {
            return this.restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity(null, headers), Game.class).getBody();
        } catch (HttpClientErrorException e) {
            log.error("Not able to get the response from url: {}", url);
            throw new RestClientException(String.format("Not able to get the response from url: %s", url), e);
        }
    }

}
