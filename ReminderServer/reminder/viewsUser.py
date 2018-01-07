import json
from django.shortcuts import render
from django.http import HttpResponse
from reminder.models import user, friend, event, eventRelationship, comment, link, linkComponent


def registe(request):
    userID = request.POST["userID"]
    password = request.POST["password"]
    Ret = {"errCode" : -1}

    userTmp = user.objects.filter(userID=userID)
    if (len(userTmp) > 0):
        Ret["errCode"] = 1
    else:
        userTmp = user.objects.create(userID = userID, password = password)
        userTmp.save()

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    return HttpResponse(json.dumps(Ret), content_type = 'application/json')



def logIn(request):
    userID = request.POST["userID"]
    password = request.POST["password"]
    Ret = {"errCode" : -1}

    if ("userID" in request.session):
        Ret["errCode"] = 3
    else:
        userTmp = user.objects.filter(userID = userID)
        if (len(userTmp) > 0):
            if (userTmp[0].password != password):
                Ret["errCode"] = 2
        else:
            Ret["errCode"] = 1

    if (Ret["errCode"] == -1):
        request.session["userID"] = userID
        Ret["errCode"] = 0
    print(json.dumps(Ret))
    return HttpResponse(json.dumps(Ret), content_type = 'application/json')



def logOut(request):
    Ret = {"errCode" : -1}

    if ("userID" in request.session):
        del request.session["userID"]
    else:
        Ret["errCode"] = 1

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    return HttpResponse(json.dumps(Ret), content_type = 'application/json')



def updatePassword(request):
    Ret = {"errCode": -1}
    userID = request.POST["userID"]
    password = request.POST["password"]

    if ("userID" in request.session):
        if (request.session["userID"] == userID):
            userTmp = user.objects.filter(userID = userID)
            if (len(userTmp) > 0):
                userTmp[0].password = password
                userTmp[0].save()
                Ret["errCode"] = 0
            else:
                Ret["errCode"] = 1
        else:
            Ret["errCode"] = 2;
    else:
        Ret["errCode"] = 3;

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    return HttpResponse(json.dumps(Ret), content_type = 'application/json')
