[![Build Status](https://travis-ci.org/wallee-payment/wallee-android-sdk.svg?branch=master)](https://travis-ci.org/wallee-payment/wallee-android-sdk)

# wallee-android-sdk

This project allows to integrate the <a href="https://wallee.com">wallee payment service</a> into
Android apps.

# Install Dependency

FIXME

# Basic Usage

The simplest way to use the SDK is using the `ActivityAwarePaymentFragment`. This fragment manages
the whole payment process. In essence the `Activity` has to implement the interfaces
`CredentialsProviderResolver` and `FragmentTerminationListener`. This fragment fetches the required
data automatically from the `Activity`.

The `FragmentTerminationListener` is responsible for handling the termination of the fragment. The
fragment terminates when either the payment process was successful or it failed.

The `CredentialsProviderResolver` is responsible for fetching the access credentials to the web
service API. The access user ID and the HMAC key of the user need to be stored securely on the
merchant backend server. They cannot be stored within the application. The backend server can
request for a particular transaction temporary credentials which grants temporary access to the
transaction. Those credentials can be forwarded to the app. This way the merchant keeps control
over what the user of the app is allowed to do.

The `ActivityAwarePaymentFragment` has generally the following flow:

1) Allow the user to select a token from a list of tokens.
2) When the user wants to use another payment method or if there are not tokens stored for the user
   a list of payment methods is presented.
3) When the user selects a payment method the form for collecting the data (like credit card number)
   is loaded.
4) The form is submitted and the transaction is processed.

This flow can be adapted see the advanced usage section for more information about this.

# Sample App

To run the sample application the easiest way is to checkout the repository and to launch the
`sample app` through `Android Studio`.

The `sample app` shows how to use the `ActivityAwarePaymentFragment`. This is the simplest way to
use the SDK.

To see how to use the `ActivityAwarePaymentFragment` the best is to take a look at the sample app.

# Advanced Usage

The SDK is build in layers:

* Credentials Handling: This layer is responsible to provide and manage the credentials.
* API Request Handling: The API request handling allows to interact with the web service API. This
  layer requires the credentials.
* Various Views: There are different views for each of the used screens.
* Flow Coordinator: The flow coordinator binds the API request handling and the views layer together.
  The flow coordinator implies a specific flow as described above.
* Fragments: The fragments we provide simplifies the usage of the flow coordinator.

Each of the above layers can be used as pleased. This implies that an app can use only the web
services and implements the rest by itself or it can also use the different views, but with a
different flow.
