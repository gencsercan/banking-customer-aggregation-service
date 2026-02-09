-- Latest record per key (e.g., LISTKEY) using analytic function
SELECT *
FROM (
  SELECT t.*,
         ROW_NUMBER() OVER (PARTITION BY t.LISTKEY ORDER BY t.SNPST_DT DESC) AS rn
  FROM   CCSOWNER.POSP_POSCUSTOMER_LIST t
)
WHERE rn = 1;
