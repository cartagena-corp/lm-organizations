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
public class RoleExternalService {

    private static final Logger logger = LoggerFactory.getLogger(RoleExternalService.class);

    @Value("${role.service.url}")
    private String roleServiceUrl;

    private final RestTemplate restTemplate;

    public RoleExternalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void initializeDefaultRoles(UUID organizationId, String token) {
        logger.debug("Inicializando roles por defecto para la organización: {}", organizationId);
        try {
            String url = String.format("%s/initialize/%s", roleServiceUrl, organizationId.toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
            logger.info("Roles por defecto creados exitosamente para la organización: {}", organizationId);
        } catch (HttpClientErrorException ex) {
            logger.error("Error al inicializar roles. Código de estado: {}. Mensaje: {}", ex.getStatusCode(), ex.getMessage());
            throw new BaseException(ConstantUtil.ROLE_INITIALIZATION_ERROR, ex.getStatusCode().value());
        } catch (ResourceAccessException ex) {
            logger.error("El servicio de roles no está disponible: {}", ex.getMessage());
            throw new BaseException(ConstantUtil.ACCESS_EXCEPTION, HttpStatus.SERVICE_UNAVAILABLE.value());
        } catch (Exception ex) {
            logger.error("Error inesperado al inicializar roles para la organización {}: {}", organizationId, ex.getMessage());
            throw new RuntimeException("Error inesperado al inicializar roles", ex);
        }
    }

    public void deleteByOrganizationId(UUID organizationId, String token) {
        logger.debug("Eliminando todos los roles para la organización: {}", organizationId);
        try {
            String url = String.format("%s/organization/%s", roleServiceUrl, organizationId.toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

            logger.info("Todos los roles eliminados exitosamente para la organización: {}", organizationId);
        } catch (HttpClientErrorException ex) {
            logger.error("Error al eliminar roles. Código de estado: {}. Mensaje: {}", ex.getStatusCode(), ex.getMessage());
            throw new BaseException(ConstantUtil.ROLE_DELETE_ERROR, ex.getStatusCode().value());
        } catch (ResourceAccessException ex) {
            logger.error("El servicio de roles no está disponible: {}", ex.getMessage());
            throw new BaseException(ConstantUtil.ACCESS_EXCEPTION, HttpStatus.SERVICE_UNAVAILABLE.value());
        } catch (Exception ex) {
            logger.error("Error inesperado al eliminar roles para la organización {}: {}", organizationId, ex.getMessage());
            throw new RuntimeException("Error inesperado al eliminar roles", ex);
        }
    }

}

