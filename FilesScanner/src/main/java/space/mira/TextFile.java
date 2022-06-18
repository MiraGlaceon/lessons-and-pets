package space.mira;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class TextFile implements Comparable {
    private String name;
    private String path;
    private String text;

    @Override
    public int compareTo(Object o) {
        TextFile other = (TextFile) o;
        return this.name.compareTo(other.name);
    }
}
