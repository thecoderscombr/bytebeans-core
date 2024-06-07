/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.certificate;

import java.nio.file.Path;

public record CertParts(Path privateKey, Path caBundleCrt, Path certificateCrt, String password) {
}
