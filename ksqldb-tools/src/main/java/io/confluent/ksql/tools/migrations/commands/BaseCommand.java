/*
 * Copyright 2020 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"; you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.tools.migrations.commands;

import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.OptionType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;

/**
 * Defines common options across all of the migration
 * tool commands.
 */
@SuppressFBWarnings(
    value = "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD",
    justification = "code is skeleton only at the moment, used to generate HELP message"
)
public abstract class BaseCommand {

  private static final String CONFIG_FILE_OPTION = "--config-file";
  private static final String CONFIG_FILE_OPTION_SHORT = "-c";

  @Option(
      name = {CONFIG_FILE_OPTION_SHORT, CONFIG_FILE_OPTION},
      title = "config-file",
      description = "Specifies a configuration file",
      type = OptionType.GLOBAL
  )
  protected String configFile;

  @Option(
      name = {"--dry-run"},
      title = "dry-run",
      description = "dry run the current command, no ksqlDB statements will be executed",
      type = OptionType.GLOBAL
  )
  protected boolean dryRun = false;

  /**
   * @return exit status of the command
   */
  public int run() {
    final long startTime = System.nanoTime();
    final int status = command();
    getLogger().info("Execution time: " + (System.nanoTime() - startTime) / 1000000000);
    return status;
  }

  /**
   * @return exit status of the command
   */
  protected abstract int command();

  protected abstract Logger getLogger();

  protected boolean validateConfigFilePresent() {
    if (configFile == null || configFile.equals("")) {
      getLogger().error("Migrations config file required but not specified. "
          + "Specify with {} (or, equivalently, {}).",
          CONFIG_FILE_OPTION, CONFIG_FILE_OPTION_SHORT);
      return false;
    }
    return true;
  }
}
