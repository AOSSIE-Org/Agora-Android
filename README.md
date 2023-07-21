# Agora Android Application

[![Pipeline Status](https://gitlab.com/aossie/agora-android/badges/develop/pipeline.svg)](https://gitlab.com/aossie/agora-android/-/commits/develop)
[![Gitter](https://img.shields.io/badge/chat-on%20gitter-ff006f.svg?style=flat-square)](https://gitter.im/AOSSIE/Agora-Android)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)


_Android Application for Agora Web that uses [Agora](https://gitlab.com/aossie/Agora/): An Electronic Voting Library implemented in Scala. This application uses [Agora Web API](https://gitlab.com/aossie/Agora-Web) as backend application_

---

#### Screenshots

<table>
    <tr>
     <td><kbd><img src="./Screenshots/GETSTARTED.jpg"></kbd></td>
     <td><kbd><img src="./Screenshots/LOGIN.jpg"></kbd></td>
     <td><kbd><img src="./Screenshots/SIGNUP.jpg"></kbd></td>
     <td><kbd><img src="./Screenshots/DASHBOARD.jpg"></kbd></td>
     <td><kbd><img src="./Screenshots/CREATE.jpg"></kbd></td>
     <tr> 
      <td><kbd><img src="./Screenshots/CALENDER.jpg"></kbd></td>
      <td><kbd><img src="./Screenshots/SETTINGS.jpg"></kbd></td>
      <td><kbd><img src="./Screenshots/PROFILE.jpg"></kbd></td>
      <td><kbd><img src="./Screenshots/CONTACT.jpg"></kbd></td>
      <td><kbd><img src="./Screenshots/DETAIL.jpg"></kbd></td>
    </tr>
  </table>

---

## Contributing Guidelines
Please refer to [CONTRIBUTING.md](https://gitlab.com/aossie/agora-android/-/blob/develop/CONTRIBUTING.md)

## Discussion
Join the discussion channels [Discord](https://discord.com/invite/6mFZ2S846n) [Gitter](https://gitter.im/AOSSIE/Agora-Android)

## Android specific guidelines
- Before committing code make sure that project [style](https://developer.android.com/studio/intro#style_and_formatting) is set to project.
- Use data-binding for any new code changes
- Write test cases ( both UI and unit as applicable for new features )
- Do-Not create any nested xml layouts, in case the view is complex to have more than two level nesting create a custom view
- Follow SOLID, clean code principles.
