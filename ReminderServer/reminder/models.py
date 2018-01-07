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
    userID = models.CharField(max_length=20)
    title = models.CharField(max_length=30)
    content = models.CharField(max_length=200)
#    picture = models.ImageField(upload_to = 'img')
    permission = models.IntegerField()
    approve = models.IntegerField()
    startTime = models.DateTimeField()
    endTime = models.DateTimeField()
    fin = models.IntegerField()
    forked = models.IntegerField()

class comment(models.Model):
    eventID = models.IntegerField()
    userID = models.CharField(max_length=20)
    content = models.CharField(max_length = 200)
    commitTime = models.DateTimeField(auto_now_add=1)







class eventRelationship(models.Model):
    eAID = models.IntegerField()
    eBID = models.IntegerField()

class link(models.Model):
    linkID = models.IntegerField()
    userID = models.CharField(max_length=20)
    title = models.CharField(max_length=30)
    content = models.CharField(max_length=200)
    remarks = models.CharField(max_length=100)
#    picture = models.ImageField(upload_to = 'img')
    Permission = models.IntegerField()
    topologyID = models.IntegerField()
    approve = models.IntegerField()

class linkComponent(models.Model):
    linkID = models.IntegerField()
    eventID = models.IntegerField()
