<div align="center"> 

# Welcome to the AR-Filter-app Repository!
 <td align="center">
      <h3></h3>
      <img src="App Screenshots/ARimage.png" width="300" height="250">
  <h3>This app has some exciting AR Filters to play with. These Filters are developed using unity and then it was exported to Android Studios to achieve more enhances in functionality.</h3>
    </td>
    </div>


### How did we do it 
1. First we created an app in Unity which has AR Filters in which the user can click the snapshot and the app stores that snap into storage at a specific location and the app terminates itself.
2. Second we created the app library and imported it to Android Studios where we made an app which takes the latest snap from the folder where the unity app stored the snaps and display it in the imageview.
3. The user also has option to upload it to FireStore not only filter images but also any image from gallery.

### How to SetUp the project in Android Studios
**Step 1.** Download this Project and open it in Android Studios.<br>
**Step 2.** Download the Unity Library from the <a href="https://drive.google.com/drive/folders/1XbMmsOlqrErZuj4fsZ2iLA2f13mKmX6H?usp=sharing">link</a>. <br>
**Step 3.** Open the settings.gradle file and on 10th line change the address to the address where you have downloaded the unity library.<br>
**Step 4.** Sync the gradle again and you are good to go.

### Error Developer may find while working on project
**1. Manifest Merger Error**<br>
To resolve this error one the unity library module select android.manifest file and reformat its code and delete the intent of unity activity and try again.<br>

**2. NDK Error**<br>
To resolve this error open the project structure under file menu, select modules ansd click on unity library and select NDK.<br>
Remember NDK we used in the project must be same NDK version used in the Unity so please make sure NDK version must be same.<br>
The version we used of NDK is **21.3.6528147**.<br>

**Note - In this app the devlopers can only contribute in the android part of the app if any user want to contribute in filter part that project is on this <a href="https://github.com/ashut0sh75/ARfilter-2">link.</a>**

**This project is eagerly waiting for excited contributers who wants to contribute. The contributer must follow some set of rules which needs to be followed while contributing.**

### Please Star this repository while contributing, if you like this project.

## Rules need to be followed

 **Step 1.** Contributer must create an issue describing the area he wants to contribute in. Contributer need to give a detailed explanation about it.<br>
**Step 2.**  After the issue has been created, he needs to wait until the task is assigned to him. Once the the issue is assigned to him, he can start working on it.<br>
**Step 3.** After working on the task contributer needs to generate a pull request. After the pull request has been generated, contributer must wait for the response.<br>

##### If you have any questions, we can communicate in issues or you can e-mail me at **ashusanugupta999@gmail.com**. 

## Below are some sample images of the app
<table align="center">
  <tr>
    <td style="padding: 20px;">
      <img src="App Screenshots/Image 1.jpg" width="300" height="500" alt="Image 1">
    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td style="padding: 20px;">
      <img src="App Screenshots/Image 2.jpg" width="300" height="500" alt="Image 2">
    </td>
  </tr>
  <tr>
    <td style="padding: 20px;">
      <img src="App Screenshots/Image 3.jpg" width="300" height="500" alt="Image 3">
    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td style="padding: 20px;">
      <img src="App Screenshots/Image 4.jpg" width="300" height="500" alt="Image 4">
    </td>
  </tr>
</table>











