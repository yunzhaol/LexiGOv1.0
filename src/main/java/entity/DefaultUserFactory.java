//package entity;
//
///** Produces CommonUser instances. */
//public final class DefaultUserFactory implements UserFactory {
//
//    @Override
//    public User create(String name, String password) {
//        return new CommonUser(name, password);
//    }
//
//    @Override
//    public User create(String name, String password,
//                       String securityQuestion,
//                       String securityAnswer) {
//        return new SecurityUser(name, password,
//                securityQuestion, securityAnswer);
//    }
//}
