package com.changan.cdc.sds.tools.jidullmsdk.llmsdk;

public class LLMAppendMsg {
   private int audioZone;
   private long requestTime;
   private long lastResponseTime;

   public long getLastResponseTime() {
      return lastResponseTime;
   }

   public void setLastResponseTime(long lastResponseTime) {
      this.lastResponseTime = lastResponseTime;
   }

   public long getRequestTime() {
      return requestTime;
   }

   public void setRequestTime(long requestTime) {
      this.requestTime = requestTime;
   }

   public int getAudioZone() {
      return audioZone;
   }

   public void setAudioZone(int audioZone) {
      this.audioZone = audioZone;
   }


}
