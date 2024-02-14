# DECIDE

## Intercepter Launch Algorithm

### Description
The program DECIDE is implemented in a function called Main(), which is a hypothetical anti-ballistic missile system which determines whether an interceptor should be launched based upon input radar tracking information. 

The program evaluates 15 Launch Intercept Conditions (LICs) with an input set of up to 100 planar data points. A Logical Connector Matrix (LCM) determines how all 15 LICs must be considered jointly, containig elements ANDD, ORR or NOTUSED. A Preliminary Unlocking Vector (PUV) determines which LIC matters in deciding whether to launch or not.

### Instructions
To run the the program, clone the repository using the following command.

`git clone https://github.com/goodtimeswithpaul/decide.git`

Open the project in VS Code, press play button on main file.

### Input
**NUMPOINTS** The number of planar data points.

**POINTS** Array containing the coordinates of data points.

**LCM** Logical Connector Matrix.

**PUV** Preliminary Unlocking Vector.

**Parameters: Struct holding parameters for LIC’s.**

1. double LENGTH1;
2. double RADIUS1;
3. double EPSILON;
4. double AREA1;
5. int Q_PTS;
6. int QUADS;
7. double DIST;
8. int N_PTS ;
9. int K_PTS ;
10. int A_PTS ;
11. int B_PTS ;
12. int C_PTS ;
13. int D_PTS ;
14. int E_PTS ;
15. int F_PTS ;
16. int G_PTS ;
17. double LENGTH2;
18. double RADUIS2;
19. double AREA2;

### Output

**LAUNCH** Final launch / no launch decision encoded as ”YES”, ”NO” on the standard output.

### Intermediate results
**CMV** Conditions Met Vector.

**PUM** Preliminary Unlocking Matrix.

**FUV** Final Unlocking Vector.

### Authors

Anneli Bogren – [annelibogren](https://github.com/annelibogren)

Paul Tissot-Daguette – [goodtimeswithpaul](https://github.com/goodtimeswithpaul)

Carl Wang - [WarlCang](https://github.com/WarlCang)

Rikard Johansson - [ItsRkaj](https://github.com/ItsRkaj)


## Statement of Contributions
We distributed the work in the following way:

**Anneli**: LIC1, LIC6, LIC8, LIC13, unit testing set up, README

**Paul**: LIC0, LIC5, LIC7, LIC12, combiner function for PUM and PUV into FUV

**Carl**: LIC2, LIC4, LIC9, LIC11, parameters set up, combiner function for CMV and LCM to PUM

**Rikard**: LIC3, LIC10, LIC14, combine into main


## Evaluation of our work
We standardized a way of naming functions and syntax, as well as how we would handle pull requests. Since we were a group of 4, we decided that at least 2 should review and approve the changes before merging into the main branch. We also set a standard for everyone to write tests for their respective LICs, including appropriate commentary. Furthermore, we linked each commit to an issue by referencing the issue in the description. Regarding branch naming, we used GitHubs automatically provided names to ensure that all branches followed the same standard.

After completing assignment 1, we believe that we are currently in the *in use state*. We completed the first two states early on as we met and agreed upon principles and constraints, as well as established a foundation for our workflow. The use stage has taken place during the coding process, as we have divided the work among us and we use the practices and tools established by the team.

In completing this assignment, we have learned some important lessons in working as a team on a version control tool. For example, we noticed how important it is to standardize commit messages to simplify searching for certain issues and clean up the repository. We also found that establishing clear deadlines is important to ensure that everyone can complete their assigned tasks, as some are more dependent on each other than others. Improving on these aspects as a team would allow us to move closer to the *in place state*.