from django.db import models

# Create your models here.

class user(models.Model):
    userID = models.CharField(max_length=20)
    password = models.CharField(max_length=20)
#    rewards = models.IntegerField()

class friend(models.Model):
    uAID = models.CharField(max_length=20)
    uBID = models.CharField(max_length=20)

class event(models.Model):
    eventID = models.IntegerField()
    title = models.CharField(max_length=30)
    content = models.CharField(max_length=200)
    permission = models.IntegerField()
    approve = models.IntegerField()
#    startTime = models.DateTimeField()
    endTime = models.DateTimeField()
    fin = models.IntegerField()
    forked = models.IntegerField()

class eventMember(models.Model):
    eventID = models.IntegerField()
    userID = models.CharField(max_length=20)


class subEvent(models.Model):
    subEventID = models.IntegerField()
    eventID = models.IntegerField()
    title = models.CharField(max_length=20)
    fin = models.IntegerField()
#    content = models.CharField(max_length=20)

class comment(models.Model):
    eventID = models.IntegerField()
    userID = models.CharField(max_length=20)
    content = models.CharField(max_length = 200)
    commitTime = models.DateTimeField(auto_now_add=1)
