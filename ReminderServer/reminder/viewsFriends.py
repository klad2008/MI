import json
from django.shortcuts import render
from django.http import HttpResponse
from reminder.models import user, friend, event, eventMember


def updateFriends(request):
    Ret = {"errCode" : -1}
    uAID = request.POST["uAID"]
    uBID = request.POST["uBID"]
    friendStatus = int(request.POST["friendStatus"])

    print(uAID, uBID, friendStatus)
    if (friendStatus == 0):
        friend.objects.filter(uAID = uAID, uBID = uBID).delete();
    else:
        friendTmp = friend.objects.filter(uAID=uAID, uBID=uBID)
        if (len(friendTmp) == 0):
            friendTmp = friend.objects.create(uAID = uAID, uBID = uBID)
            friendTmp.save()

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    response = HttpResponse(json.dumps(Ret), content_type='application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response



def checkFriendStatus(request):
    Ret = {"errCode" : -1}
    uAID = request.POST["uAID"]
    uBID = request.POST["uBID"]

    print(uAID, uBID)
    friendList = friend.objects.filter(uAID = uAID, uBID = uBID)
    Ret["status"] = (len(friendList) > 0)

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    response = HttpResponse(json.dumps(Ret), content_type='application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response


def queryFriends(request):
    UserID = request.POST["userID"]
    Ret = {"errCode" : -1}

    print(UserID)

    FriendsList = friend.objects.filter(uAID = UserID)
    FriendsList2 = []
    for FriendTmp in FriendsList:
        Tmp = {}
        Tmp["uBID"] = FriendTmp.uBID
        FriendsList2.append(Tmp)

    Ret["friends"] = FriendsList2
    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type='application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response