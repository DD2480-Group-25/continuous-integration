# Continuous Integration

### Description
This project implements a small continuous integration server using Gradle, containing only the core features of continuous integration.

### Use
Clone
```bash
git clone https://github.com/DD2480-Group-25/continuous-integration.git
```

### Run
Open project in Intellij and press run button

### Test
```bash
./gradlew test
```
We tested our implementations by creating test repositories and simulating instances where, for example, the build should for or a notification should be sent.

### Build
```bash
./gradlew build
```

### Authors

Anneli Bogren – [annelibogren](https://github.com/annelibogren)

Paul Tissot-Daguette – [goodtimeswithpaul](https://github.com/goodtimeswithpaul)

Carl Wang - [WarlCang](https://github.com/WarlCang)

Rikard Johansson - [ItsRkaj](https://github.com/ItsRkaj)

## Statement of Contributions
We distributed the work in the following way:

**Anneli**: Build implementation, README

**Paul**: Clone/Fetch implementation, webhook

**Carl**: Notification of CI results support

**Rikard**: Gradle set up, testing

**REMARKBLE**: The notification system is not using a popular tool (such as Jenkins), to easily update the commit status. It uses the so called "old-fashion" way to make HTTP request
using a client due to learning purpose. In order to achieve this, the provided Github commit status documentation and java HTTP library is repeatedly accessed. The learning outcome is
that the authors gained better knowledge of HTTP request and response in practice.  

Moreover, some extra work has been put in implementing the part about the Git handling. Indeed, it would have been 
easier to simply issue commands using a process builder, call the 2-3 commands necessary and be done with it.
The problem with this method is that it is very easily injectable by a malevolent actor. All one would have to do is 
to create a fake webhook request in which the branch command is an escape character followed by a malicious command
(like some one-liner opening a reverse shell), and an attacker would be able to take control of the machine running the
CI. That is why we thought it was important to do things in a clean fashion using a well-established library (JGit), in
order to meet basic security criteria, which are critical in a production environment. This allowed us to practice reasoning 
about web security, as well as to use libraries written by other developers instead of reimplementing things in a 
clumsy and dangerous way.
### Essence Evaluation of Our Team

In evaluating our team in the essence standard, we believe that we are currently in the collaborating stage. We believe that we have passed the seeded stage as the composition of the team has been clearly defined and each member's responsibilities regarding this lab were outlined after the first meeting. We did this by assigning each member with a specific milestone that related to the different aspects of the lab. For example, one member worked on implementing the cloning/fetching aspect while another worked on the update report which the CI sends. We decided on not having a set leader role and rather delegated responsibilities evenly. Furthermore, we believe that we have passed the formed stage as we have defined communication mechanisms and team members are accepting their work. In regards to the collaborating stage, we are starting to work as one cohesive unit, though our tasks have been divided among us. We are focused on achieving the mission and try to encourage open and honest communication among us by requiring at least one review on all pull requests.
The next stage for us is the performing stage. We have done quite well in adapting to changing context and identifying problems without seeking external help. However, the most prominent obstacle for us right now is consistently meeting commitments, as our differing schedules can contribute to difficulties in sticking to deadlines we establish within the group.