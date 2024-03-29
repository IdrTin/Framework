# Team 3648 Java FRC Framework

Welcome to team 3648's Java FRC Framework! This is a command-based framework that allows teams to easily program their robot using XML for FIRST Robotics Competition (FRC). This framework is designed to simplify the programming process and make it easier for teams to focus on building their robot.
## Features
<ul>
    <li>Uses XML files to store hardware constants such as ports, motor controller type, control mode, etc.</li>
    <li>Controller mapping for easy joystick input mapping.</li>
    <li>Autonomous program writing using a command-based system.</li>
    <li>Generic commands like Motor_Set or Motor Default, as well as specialized subsystem commands that can be added.</li>
</ul>

## Getting Started

### Prerequisites

<ul>
    <li>WPILib</li>
    <li>Java Development Kit (JDK) version 11 or higher.</li>
    <li>FRC RoboRio and a compatible hardware.</li>
</ul>

## Installation

<ol>
    <li>Clone or download the repository.</li>
    <li>Open the project in your preferred Java IDE.</li>
    <li>Build the project using Maven.</li>
</ol>

## Usage

<ol>
    <li>Define your robot hardware constants in src/main/java/frc/robot/Robot.java file.</li>
    <li>Create your autonomous programs in the src/main/deploy/auton folder.</li>
    <li>Define your control mappings in the src/main/deploy/controller folder.</li>
    <li>Deploy the code to your robot and run it.</li>
    <li>Monitor the robot through shuffle board and terminal.</li>
</ol>

For more information on how to use the Framework, please visit out [wiki](https://github.com/3648TJSpartans/Framework/wiki).

## Contributing

We welcome contributions from other teams who are interested in using or improving our framework. If you have suggestions or improvements, please feel free to fork the repository and submit a pull request.

## Acknowledgments

We would like to thank the FIRST Robotics Competition and all the volunteers who make it possible. We would also like to thank the developers of WPILib for their contributions to the FRC community.

