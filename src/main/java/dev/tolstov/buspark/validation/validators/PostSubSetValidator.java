package dev.tolstov.buspark.validation.validators;

import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.validation.constraints.PostSubset;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PostSubSetValidator implements ConstraintValidator<PostSubset, Employee.Post> {
    private List<Employee.Post> validPosts;

    @Override
    public void initialize(PostSubset constraintAnnotation) {
        this.validPosts = Arrays.asList(constraintAnnotation.anyOf());
    }

    @Override
    public boolean isValid(Employee.Post post, ConstraintValidatorContext constraintValidatorContext) {
        return post == null || validPosts.contains(post);
    }
}
