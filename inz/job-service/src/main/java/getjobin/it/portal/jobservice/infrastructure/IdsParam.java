package getjobin.it.portal.jobservice.infrastructure;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class IdsParam {

    private String commaSeparatedIds;

    public IdsParam(String commaSeparatedIds) {
        this.commaSeparatedIds = commaSeparatedIds;
    }

    public List<Long> asList() {
        return Arrays.stream(commaSeparatedIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public Set<Long> asSet() {
        return Arrays.stream(commaSeparatedIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toSet());
    }
}
