USE [bd_ig-projet]
GO
/****** Object:  UserDefinedFunction [dbo].[TRIM]    Script Date: 03/04/2017 12:32:33 p.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[TRIM] 
   (  @string	 VARCHAR(MAX) 
   ) 
   RETURNS VARCHAR(MAX)
AS 
BEGIN

  RETURN LTRIM(RTRIM(@string))
  
END
