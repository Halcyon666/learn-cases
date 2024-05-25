package com.whalefall.upsert;

import com.whalefall.learncases.upsert.entity.MyEntity;
import com.whalefall.learncases.upsert.entity.MyInsertEntity;
import com.whalefall.learncases.upsert.entity.MyUpdateEntity;
import com.whalefall.learncases.upsert.repository.Repository;
import com.whalefall.learncases.upsert.service.CommonUpsertService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class UpsertTests {

    private static Stream<Arguments> provideEntities() {
        return Stream.of(
                Arguments.of(MyEntity.builder().id("123").hasQueryResult(true).build(), true),
                Arguments.of(MyEntity.builder().id("123").hasQueryResult(false).build(), false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideEntities")
    void testUpsert(MyEntity entity, boolean hasQueryResult) {
        Repository myEntityRepository = mock(Repository.class);
        CommonUpsertService<MyEntity, MyInsertEntity, MyUpdateEntity> commonUpsertService = new CommonUpsertService<>();

        // Mock repository behavior
        when(myEntityRepository.query(any(MyEntity.class)))
                .thenReturn(hasQueryResult ? Optional.of(Boolean.TRUE) : Optional.empty());

        // Create captors for argument verification
        ArgumentCaptor<MyInsertEntity> insertCaptor = ArgumentCaptor.forClass(MyInsertEntity.class);
        ArgumentCaptor<MyUpdateEntity> updateCaptor = ArgumentCaptor.forClass(MyUpdateEntity.class);

        // Invoke upsert method
        commonUpsertService.upsert(entity, myEntityRepository);

        if (hasQueryResult) {
            verify(myEntityRepository).update(updateCaptor.capture());
            MyUpdateEntity capturedUpdate = updateCaptor.getValue();
            assert capturedUpdate.getId().equals(entity.getId());
            assert capturedUpdate.getAge() == 18;
        } else {
            verify(myEntityRepository).insert(insertCaptor.capture());
            MyInsertEntity capturedInsert = insertCaptor.getValue();
            assert capturedInsert.getId().equals(entity.getId());
            assert capturedInsert.getName().equals("Halcyon");
        }
    }
}
