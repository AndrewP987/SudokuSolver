package backtracking;

import java.util.List;
import java.util.Optional;

public class Backtracker {

    private int configCount;

    public Backtracker() {
        this.configCount = 1;  // counts the initial config sent to solve()
    }

    public Optional<Configuration> solve(Configuration config) {
        if (config.isGoal()) {
            return Optional.of(config);
        } else {
            List<Configuration> successors = config.getSuccessors();
            configCount += successors.size();
            for (Configuration currSuccessor : successors) {
                if (currSuccessor.isValid()) {
                    Optional<Configuration> sol = solve(currSuccessor);
                    if (sol.isPresent()) {
                        return sol;
                    }
                }
            }
        }
        return Optional.empty();
    }

    public int getConfigCount() {
        return this.configCount;
    }
}
