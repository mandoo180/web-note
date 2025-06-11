(require 'org)
(require 'ox-html)

(setq org-html-validation-link nil
	  org-html-head-include-scripts nil
	  org-html-head-include-default-style nil
	  org-export-with-author nil)

(let ((org-files argv))
  (dolist (org-file org-files)
	(when (file-exists-p org-file)
		(with-current-buffer (find-file-noselect org-file)
		  (princ (org-export-as 'html nil t t))))))
