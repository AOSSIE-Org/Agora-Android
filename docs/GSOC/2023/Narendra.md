# GSoC 2023 Final Report - Agora Vote Android

## Student Information
- **Name:** Narendra Singh Anjana
- **GitHub:** [GitHub Profile](https://github.com/narendraanjana09)
- **LinkedIn:** [LinkedIn Profile](https://www.linkedin.com/in/narendra-singh-anjana-454bb6190/)

## Project Overview
- **Organization:** [Australian Open Source Software Innovation and Education (AOSSIE)](https://www.aossie.org/)
- **Project:** [Agora Vote Android](https://github.com/AOSSIE-Org/Agora-Android)
- **Project Duration:** 175 hours

## About Agora Vote Android
Agora Vote Android is an Android application that enables users to create elections, invite voters, vote, and view results. The project focused on implementing Clean Architecture and Kotlin Flow to enhance the application's functionality.

## Project Details
1. **Migration to Jetpack Compose and Material You Support:**
   - Migrated the user interface to Jetpack Compose for a modern and flexible UI.
   - Added Material You support for an adaptive and cohesive design.

2. **Implement CI/CD Pipeline:**
   - Introduced a CI/CD pipeline to automate the Android release process.
   - Transitioned from GitLab to GitHub for streamlined development and collaboration.

3. **Share Election Results with Visualization:**
   - Added functionality to share election results along with visualizations.
   - Improved user experience by presenting data in a meaningful and comprehensible way.

## APK: [release.apk](https://drive.google.com/file/d/1IRLhe8m58xzRSbUaiBJOjAw4k5bSm0q6/view?usp=sharing)

## UI Redesign
Apart from the technical implementations, I also redesigned the user interface using Figma, aligning it with the new Jetpack Compose and Material You design principles.
- [Figma UI](https://www.figma.com/file/Lf1vuJoOCerc5LauS76Kcz/Agora-Vote-App?type=design&node-id=1316-926&mode=design)

## Challenges Faced
- Adapting Kotlin code for newer Android versions.
  - The ever-evolving Android ecosystem presented a challenge when it came to updating the existing Kotlin code to work seamlessly with newer Android versions. Changes in API behavior and deprecations required careful consideration to maintain compatibility without compromising functionality. 
- Incorporating libraries to customize Compose code for functionalities not natively supported.
  - While Jetpack Compose brought significant benefits to UI development, some functionalities required custom implementations as they were not natively supported by Compose libraries. This challenge involved identifying suitable third-party libraries, integrating them, and aligning them with the Compose architecture.
- Migrating CI/CD process from GitLab to GitHub.
  - The shift from GitLab to GitHub for CI/CD processes brought about a series of complexities. Transferring pipelines, setting up integrations, and ensuring a smooth deployment pipeline required meticulous planning and execution.

## Merged Pull Requests
- [#10](https://github.com/AOSSIE-Org/Agora-Android/pull/10), [#11](https://github.com/AOSSIE-Org/Agora-Android/pull/11), [#16](https://github.com/AOSSIE-Org/Agora-Android/pull/16), [#17](https://github.com/AOSSIE-Org/Agora-Android/pull/17), [#18](https://github.com/AOSSIE-Org/Agora-Android/pull/18), For a complete list of merged pull requests, you can refer here [GitHub My Pull Requests](https://github.com/AOSSIE-Org/Agora-Android/pulls?q=is%3Apr+is%3Aopen+user%3Anarendraanjana09).
 

## Future Plans
- Plan to migrate navigation from fragment navigation to complete Compose navigation.
- Addressing any existing issues and bugs identified during development and testing phases to ensure a robust and glitch-free user experience.

## Acknowledgments
I would like to extend my gratitude to my mentor [Amit Kumar](https://www.linkedin.com/in/amit3210/) for his consistent guidance and support throughout the GSoC journey. I'd also like to thank Abhishek Agarwal for his occasional assistance.

## Conclusion
Participating in Google Summer of Code with AOSSIE has been an incredible learning experience. I not only enhanced my technical skills but also contributed to a project that has real-world impact. The challenges I faced and overcame have made me a more resilient and adaptable developer. I'm excited to continue my journey in the open-source community and explore new avenues of innovation.

##
A big thank you to AOSSIE and the GSoC community for this invaluable opportunity. The knowledge and growth achieved throughout this journey will stay with me, guiding my path ahead.
<br>
<br>
Warmly,<br>
Narendra Singh Anjana
