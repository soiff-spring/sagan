package sagan.git;

/**
 * Created by soiff on 27/08/2017.
 *
 * @author : soiff
 * @version : 1.0
 * @since : 1.8
 */
public interface GitClientBuilder<T> {
    GitClient build(T api);
}
