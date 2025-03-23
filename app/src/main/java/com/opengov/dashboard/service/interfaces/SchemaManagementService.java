package com.opengov.dashboard.service.interfaces;

import javax.sql.DataSource;

/**
 * 
 * Provide APIs for database migration which will be implemented by different migration providers
 */
public interface SchemaManagementService {

  /**
   * This function Run DDL script on DB as per provider Implementation
   * 
   * @param dataSource DataSource on which script to be execute
   * @param schemaName Schema name in DB to execute the script
   * @param changeLogFile DDL script file path
   */
  void applyChangelog(DataSource dataSource, String schemaName, String changeLogFile);
}
