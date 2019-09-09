package getjobin.it.portal.jobservice.infrastructure;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IdsParam {

    public static final String ID_PATH = "{id}";
    public static final String ID = "id";
    public static final String IDS_PATH = "{ids}";
    public static final String IDS = "ids";

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
