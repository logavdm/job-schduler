INSERT INTO JOB(ID,NAME,EXPRESSION,ENABLED,JOB_CLASS,STATUS,JOB_RUN_LOG_ENABLED,CREATEDAT,UPDATEDAT) VALUES(1,'DEMO EVERY SEC JOB','* * * * * *',0,'com.loga.runnerpkg.DemoRunner','STOPPED',0,NOW(),NOW());
INSERT INTO JOB(ID,NAME,EXPRESSION,ENABLED,JOB_CLASS,STATUS,JOB_RUN_LOG_ENABLED,CREATEDAT,UPDATEDAT) VALUES(2,'DEMO EVERY 2 SEC JOB','*/3 * * * * *',0,'com.loga.runnerpkg.DemoRunner','STOPPED',0,NOW(),NOW());

--INSERT INTO JOB_RUN_DETAILS(ID,DESCRIPTION,JOB_ID,CREATEDAT,UPDATEDAT) VALUES(1,'DEMO RUN 1',1,NOW(),NOW());
--INSERT INTO JOB_RUN_DETAILS(ID,DESCRIPTION,JOB_ID,CREATEDAT,UPDATEDAT) VALUES(2,'DEMO RUN 1',2,NOW(),NOW());