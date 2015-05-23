# AtisPrint
  Android application for printing to ATIS printers on the KIT Campus in Karlsruhe, Germany.

  You can find the latest apk in the root of this repo.

#State of the project
  At the moment you can print via intents. Just open a .pdf file (for example with the built-in Google PDF viewer) and select     Send to 'Atis Druck'.
  You will then be prompted for your ATIS account credentials, the file will be copied (via scp) to your home directory (on the   ATIS-server) and printed with printer sw1. (via ssh)
  If you decide to save your password it will be encrypted with your Android DEVICE_ID.

#TODO:
  - Implement menu to select which printer to print to. 
  - Add possibility to delete account
  - Delete file after printing
  
#Account safety:
  Your ATIS account is only used for authentication via ssh and scp.
  If you are rooted it is *theoretically* possible that another app could decrypt the stored password using your Android         DEVICE_ID. To counteract this (and to be 99.9% secure) spoof your DEVICE_ID for use with AtisPrint. This **only** applies if     you are rooted (and even then, someone would have to write an app and install it on your device to get your credentials)
