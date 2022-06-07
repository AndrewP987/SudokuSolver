package backtracking;

import java.util.List;

/**
 * Configuration Interface
 *
 * @date June 7 2022
 * @author Andrew Photinakis
 */
public interface Configuration {
    List<Configuration> getSuccessors();
    boolean isValid();
    boolean isGoal();
}
