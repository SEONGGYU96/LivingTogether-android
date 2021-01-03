# LivingTogether
![living_together_graphic_img](https://user-images.githubusercontent.com/57310034/102754513-74558980-43b0-11eb-9c10-00518a967ef6.png)
<p float="left">
<img alt="living_together_screenshot_1"  width="150" src="https://user-images.githubusercontent.com/57310034/102754584-8c2d0d80-43b0-11eb-9bb5-ed6ae51094a0.png"/>

<img alt="living_together_screenshot_2"  width="150" src="https://user-images.githubusercontent.com/57310034/102754583-8b947700-43b0-11eb-8f8c-b4636c20db88.png"/>

<img alt="living_together_screenshot_3"  width="150" src="https://user-images.githubusercontent.com/57310034/102754582-8a634a00-43b0-11eb-8265-723032d15ba8.png"/>

<img alt="living_together_screenshot_4"  width="150" src="https://user-images.githubusercontent.com/57310034/102754580-8a634a00-43b0-11eb-87a6-8d32c89922cf.png"/>

<img alt="living_together_screenshot_5"  width="150" src="https://user-images.githubusercontent.com/57310034/102754567-859e9600-43b0-11eb-9ea9-27dd0fb6364a.png"/>

<img alt="living_together_screenshot_6"  width="150" src="https://user-images.githubusercontent.com/57310034/102754557-846d6900-43b0-11eb-925b-3b730a5a7734.png"/>
</P>

[![GooglePlayStore](https://img.shields.io/badge/GooglePlayStore-v1.1.0-green)](https://play.google.com/store/apps/details?id=com.seoultech.livingtogether_android)

The Android application of Emergency situation management home IoT system for the vulnerable, such as the elderly living alone.  

LivingTogether project was planned by BlueDev for gradution portfolio. This repository contains only Android application of whole project. It can detect bluetooth signals as `BLE`, from our own sensor when user use it. The signals are stored in both local database, `Room` and `Firebase Realtime Database`. The application determin Whether or not the user is unconscious using thoes stored signals. In case of an emergency, the application send this information to server(`Firebase Realtime Database`). It also send SMS to next of kin who are registered in local database(`Room`) by the user.  

---

## Install

You can install from [GooglePlayStore](https://img.shields.io/badge/GooglePlayStore-v1.1.0-green)  

Dependency
Required Android version : 23+  
Recommended Android version : 30

## Usage

1. Follow the instructions to enter your profile (name, phone number, address) after installing the app for the first time.
2. Press the register sensor button on the main screen to register the sensor.
3. (Optioanl) Press the add next of kin button on the main screen to add next of kin. You may enter name and phone number of next of kin who will be notified when you are in emergency.
4. Don't terminate this application.

## Configuration

- Archetecture : `MVVM`
- Database : `Repository`
    - Remote : `Firebase Realtime Database`
    - Local : `Room`
- Technical tag : `BluetoothLowEnerge`, `LiveData`, `ForegroundService`, `Lottie`, `CustomView`

## Contributing
- Developer : [SEONGGYU](https://github.com/SEONGGYU96)
- Designer : [Lee Yeongjin](https://www.behance.net/yeongjinlee)



