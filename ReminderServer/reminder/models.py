from django.db import models

class user(models.Model):
    userID = models.CharField(max_length=100)
    password = models.CharField(max_length=100)

class friend(models.Model):
    uAID = models.CharField(max_length=100)
    uBID = models.CharField(max_length=100)

class event(models.Model):
    eventID = models.IntegerField()
    title = models.CharField(max_length=100)
    permission = models.IntegerField()
    endTime = models.DateTimeField()
    fin = models.IntegerField()
    forked = models.IntegerField()

class eventMember(models.Model):
    eventID = models.IntegerField()
    userID = models.CharField(max_length=100)


class subEvent(models.Model):
    subEventID = models.IntegerField()
    eventID = models.IntegerField()
    title = models.CharField(max_length=100)
    fin = models.IntegerField()
    note = models.CharField(max_length = 100)
