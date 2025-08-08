package use_case.signup;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import use_case.signup.validation.Processor;

import static org.junit.jupiter.api.Assertions.*;

class SignUpProcessorBuilderTest {

    @Test
    void build_shouldThrowException_whenNoProcessorsConfigured() {
        // Arrange
        SignUpProcessor.Builder builder = SignUpProcessor.builder();

        // Act
        Executable action = builder::build;

        // Assert
        IllegalStateException exception =
                assertThrows(IllegalStateException.class, action);
        assertEquals("No processors configured.", exception.getMessage());
    }

    @Test
    void addOtherCheck_shouldAddProcessorSuccessfully() {
        // Arrange
        SignUpProcessor.Builder builder = SignUpProcessor.builder();
        Processor mockProcessor = Mockito.mock(Processor.class);

        // Act
        builder.addOtherCheck(mockProcessor);
        Processor chainHead = builder.build();

        // Assert
        assertSame(mockProcessor, chainHead, "The head of chain should be the same as the added processor");
    }
}
