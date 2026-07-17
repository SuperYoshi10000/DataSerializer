package local.ytk.app.ds.res;

import local.ytk.app.ds.val.DataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Result<T> {
    @Nullable T get();
    
    default @NotNull T getNonNull() {
        T value = get();
        if (value == null) throw new NullPointerException("Result is null");
        return value;
    }
    
    default String message() {
        return String.valueOf(get());
    }
    
    boolean successful();
    default boolean failed() {
        return !successful();
    }
    
    default Result<@NotNull T> failIfNull() {
        return get() == null ? Result.failure() : this;
    }
    
    default Optional<T> optional() {
        return Optional.ofNullable(get());
    }
    default Stream<T> stream() {
        return Stream.ofNullable(get());
    }
    default <R> Result<R> map(Function<T, R> mapper) {
        if (failed()) return failure();
        return Result.success(mapper.apply(get()));
    }
    default <R> Result<R> flatMap(Function<T, Result<R>> mapper) {
        if (failed()) return failure();
        return mapper.apply(get());
    }
    default T orElse(T other) {
        if (failed()) return other;
        return this.get();
    }
    
    
    static <T> Result<T> success(@Nullable T result) {
        return new Success<>(result);
    }
    static <T> Result<T> from(Optional<T> optional) {
        return optional.map(Result::success).orElse(Result.failure());
    }
    static <T> Result<T> ofNullable(@Nullable T value) {
        return value == null ? Result.failure() : Result.success(value);
    }
    
    @SuppressWarnings("unchecked")
    static <T> Result<T> failure() {
        return (Result<T>) FAILURE;
    }
    static <T> Result<T> failure(String message) {
        return new Failed<>(message);
    }
    
    static <T> Result<List<T>> some(Collection<Result<T>> results) {
        List<T> values = results.stream().flatMap(Result::stream).toList();
        return values.isEmpty() ? Result.failure() : Result.success(values);
    }
    static <T> Result<List<T>> all(Collection<Result<T>> results) {
        if (results.stream().anyMatch(Result::failed)) return Result.failure(results.stream().filter(Result::failed).reduce("", (String a, Result<T> b) -> a + "\n" + b.message(), String::concat));
        return Result.success(results.stream().map(Result::get).toList());
    }
    
    Result<?> FAILURE = new Result<>(){
        @Override
        public Object get() {
            return null;
        }
        
        @Override
        public @NotNull Object getNonNull() {
            throw new NullPointerException("Result is failure");
        }
        
        @Override
        public boolean successful() {
            return false;
        }
        
        @Override
        public Result<Object> failIfNull() {
            return this;
        }
        
        @Override
        public String message() {
            return "";
        }
    };
    record Success<T>(@Nullable T value) implements Result<T>, DataValue<T> {
        
        @Override
        public T get() {
            return value;
        }
        
        @Override
        public @NotNull T getNonNull() {
            if (value == null) throw new NullPointerException("Result is null");
            return value;
        }
        @Override
        public boolean successful() {
            return true;
        }
    }
    record Failed<T>(String message) implements Result<T>, DataValue<T> {
        
        @Override
        public @Nullable T get() {
            return null;
        }
        
        @Override
        public @NotNull T getNonNull() {
            throw new NullPointerException(message);
        }
        
        @Override
        public boolean successful() {
            return false;
        }
        @Override
        public Result<T> failIfNull() {
            return this;
        }
    }
}
