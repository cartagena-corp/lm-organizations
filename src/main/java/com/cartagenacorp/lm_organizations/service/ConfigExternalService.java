package com.cartagenacorp.lm_organizations.service;

import com.cartagenacorp.lm_organizations.exception.BaseException;
import com.cartagenacorp.lm_organizations.util.ConstantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class ConfigExternalService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigExternalService.class);

    @Value("${config.service.url}")
    private String configServiceUrl;

    private final RestTemplate restTemplate;

    public ConfigExternalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void initializeDefaultProjectStatus(UUID organizationId, String token) {
        logger.debug("Inicializando configuraciones por defecto para la organización: {}", organizationId);
        try {
            String url = String.format("%s/initialize/%s", configServiceUrl, organizationId.toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
            logger.info("Configuraciones por defecto creadas exitosamente para la organización: {}", organizationId);
        } catch (HttpClientErrorException ex) {
            logger.error("Error al inicializar configuraciones. Código de estado: {}. Mensaje: {}", ex.getStatusCode(), ex.getMessage());
            throw new BaseException(ConstantUtil.CONFIG_INITIALIZATION_ERROR, ex.getStatusCode().value());
        } catch (ResourceAccessException ex) {
            logger.error("El servicio de configuración no está disponible: {}", ex.getMessage());
            throw new BaseException(ConstantUtil.ACCESS_EXCEPTION, HttpStatus.SERVICE_UNAVAILABLE.value());
        } catch (Exception ex) {
            logger.error("Error inesperado al inicializar configuración para la organización {}: {}", organizationId, ex.getMessage());
            throw new RuntimeException("Error inesperado al inicializar configuración", ex);
        }
    }

    public void deleteByOrganizationId(UUID organizationId, String token) {
        logger.debug("Eliminando todas las configuraciones para la organización: {}", organizationId);
        try {
            String url = String.format("%s/organization/%s", configServiceUrl, organizationId.toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

            logger.info("Todas las configuraciones eliminadas exitosamente para la organización: {}", organizationId);
        } catch (HttpClientErrorException ex) {
            logger.error("Error al eliminar la configuración. Código de estado: {}. Mensaje: {}", ex.getStatusCode(), ex.getMessage());
            throw new BaseException(ConstantUtil.CONFIG_DELETE_ERROR, ex.getStatusCode().value());
        } catch (ResourceAccessException ex) {
            logger.error("El servicio de configuración no está disponible: {}", ex.getMessage());
            throw new BaseException(ConstantUtil.ACCESS_EXCEPTION, HttpStatus.SERVICE_UNAVAILABLE.value());
        } catch (Exception ex) {
            logger.error("Error inesperado al eliminar configuración para la organización {}: {}", organizationId, ex.getMessage());
            throw new RuntimeException("Error inesperado al eliminar configuración", ex);
        }
    }
}
