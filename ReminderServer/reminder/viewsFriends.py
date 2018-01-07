import json
from django.shortcuts import render
from django.http import HttpResponse
from reminder.models import user, friend, event, eventRelationship, comment, link, linkComponent


def updateFriends(request):
    Ret = {"errCode" : -1}
    uBID = request.POST["uBID"]
    friendStatus = int(request.POST["friendStatus"])

    if ("userID" in request.session):
        uAID = request.session["userID"]
        userTmp = user.objects.filter(userID=uBID)
        if (len(userTmp) == 0):
            Ret["errCode"] = 1
        else:
            print(uAID, uBID, friendStatus)
            if (friendStatus == 0):
                friendTmp = friend.objects.filter(uAID=uAID, uBID=uBID)
                print(friendTmp)
                print(len(friendTmp))
                if (len(friendTmp) > 0):
                    friend.objects.filter(uAID = uAID, uBID = uBID).delete();
                else:
                    Ret["errCode"] = 2
            else:
                friendTmp = friend.objects.filter(uAID=uAID, uBID=uBID)
                print(friendTmp)
                print(len(friendTmp))
                if (len(friendTmp) > 0):
                    Ret["errCode"] = 2
                else:
                    friendTmp = friend.objects.create(uAID = uAID, uBID = uBID)
                    friendTmp.save()
    else:
        Ret["errCode"] = 3

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    return HttpResponse(json.dumps(Ret), content_type = 'application/json')


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
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response