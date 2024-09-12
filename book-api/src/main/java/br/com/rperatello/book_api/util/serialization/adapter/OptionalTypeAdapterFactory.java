package br.com.rperatello.book_api.util.serialization.adapter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class OptionalTypeAdapterFactory implements TypeAdapterFactory {

	@SuppressWarnings("unchecked")
	@Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();

        if (typeToken.getRawType() == Optional.class && type instanceof ParameterizedType) {
            Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];

            TypeAdapter<?> elementAdapter = gson.getAdapter(TypeToken.get(elementType));

            return (TypeAdapter<T>) new OptionalTypeAdapter<>(elementAdapter);
        }

        return null;
    }

    private static class OptionalTypeAdapter<E> extends TypeAdapter<Optional<E>> {
        private final TypeAdapter<E> elementAdapter;

        OptionalTypeAdapter(TypeAdapter<E> elementAdapter) {
            this.elementAdapter = elementAdapter;
        }

        @Override
        public void write(JsonWriter out, Optional<E> value) throws IOException {
            if (value.isPresent()) {
                elementAdapter.write(out, value.get());
            } else {
                out.nullValue();
            }
        }

        @Override
        public Optional<E> read(JsonReader in) throws IOException {
            return Optional.ofNullable(elementAdapter.read(in));
        }
    }
    
}