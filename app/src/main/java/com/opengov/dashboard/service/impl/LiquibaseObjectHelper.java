package com.opengov.dashboard.service.impl;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.resource.ClassLoaderResourceAccessor;

public class LiquibaseObjectHelper {


  public Liquibase getLiquiBaseInstance(String changeLogFile, Database database) {
    return new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
  }

  public KubernetesClient getKubernetesClient() {
    return new DefaultKubernetesClient();
  }

}
