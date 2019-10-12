package foo.bar.example.fore.fullapp02.utils

typealias Success = () -> Unit
typealias SuccessWithPayload<T> = (T) -> Unit
typealias Failure = () -> Unit
typealias FailureWithPayload<T> = (T) -> Unit
typealias Continue = () -> Unit
typealias ContinueWithPayload<T> = (T) -> Unit
