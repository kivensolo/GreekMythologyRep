package com.mplayer;

public interface IExtMplayer {
    public String createPlayer(String x, String y, String width, String height);
    public String playVideo(String playerId, String videoId, String videoIndex, String position, String source);
    public String playVideoByUrl(String playerId, String playUrl, String position);
    public String pause(String playerId);
    public String stop(String playerId);
    public String play(String playerId);
    public String destory(String playerId);
    public String getDuration(String playerId);
    public String getCurrentPosition(String playerId);
    public String seekTo(String playerId, String pos);
    public String getPlayerState(String playerId);
    public String fullScreen(String playerId);
    public String setPlayerPosAndWH(String playerId, String x, String y, String width, String height);
}
